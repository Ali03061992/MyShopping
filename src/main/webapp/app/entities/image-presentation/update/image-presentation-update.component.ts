import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ImagePresentationFormService, ImagePresentationFormGroup } from './image-presentation-form.service';
import { IImagePresentation } from '../image-presentation.model';
import { ImagePresentationService } from '../service/image-presentation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-image-presentation-update',
  templateUrl: './image-presentation-update.component.html',
})
export class ImagePresentationUpdateComponent implements OnInit {
  isSaving = false;
  imagePresentation: IImagePresentation | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ImagePresentationFormGroup = this.imagePresentationFormService.createImagePresentationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected imagePresentationService: ImagePresentationService,
    protected imagePresentationFormService: ImagePresentationFormService,
    protected productService: ProductService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imagePresentation }) => {
      this.imagePresentation = imagePresentation;
      if (imagePresentation) {
        this.updateForm(imagePresentation);
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
    const imagePresentation = this.imagePresentationFormService.getImagePresentation(this.editForm);
    if (imagePresentation.id !== null) {
      this.subscribeToSaveResponse(this.imagePresentationService.update(imagePresentation));
    } else {
      this.subscribeToSaveResponse(this.imagePresentationService.create(imagePresentation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImagePresentation>>): void {
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

  protected updateForm(imagePresentation: IImagePresentation): void {
    this.imagePresentation = imagePresentation;
    this.imagePresentationFormService.resetForm(this.editForm, imagePresentation);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      imagePresentation.imagesPresentation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.imagePresentation?.imagesPresentation)
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
