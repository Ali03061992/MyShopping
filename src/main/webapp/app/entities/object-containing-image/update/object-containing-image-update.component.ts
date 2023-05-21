import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ObjectContainingImageFormService, ObjectContainingImageFormGroup } from './object-containing-image-form.service';
import { IObjectContainingImage } from '../object-containing-image.model';
import { ObjectContainingImageService } from '../service/object-containing-image.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-object-containing-image-update',
  templateUrl: './object-containing-image-update.component.html',
})
export class ObjectContainingImageUpdateComponent implements OnInit {
  isSaving = false;
  objectContainingImage: IObjectContainingImage | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ObjectContainingImageFormGroup = this.objectContainingImageFormService.createObjectContainingImageFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected objectContainingImageService: ObjectContainingImageService,
    protected objectContainingImageFormService: ObjectContainingImageFormService,
    protected productService: ProductService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objectContainingImage }) => {
      this.objectContainingImage = objectContainingImage;
      if (objectContainingImage) {
        this.updateForm(objectContainingImage);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('myShoppingApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objectContainingImage = this.objectContainingImageFormService.getObjectContainingImage(this.editForm);
    if (objectContainingImage.id !== null) {
      this.subscribeToSaveResponse(this.objectContainingImageService.update(objectContainingImage));
    } else {
      this.subscribeToSaveResponse(this.objectContainingImageService.create(objectContainingImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjectContainingImage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(objectContainingImage: IObjectContainingImage): void {
    this.objectContainingImage = objectContainingImage;
    this.objectContainingImageFormService.resetForm(this.editForm, objectContainingImage);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      objectContainingImage.json
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.objectContainingImage?.json)
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
