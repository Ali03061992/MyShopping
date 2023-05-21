import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FamilleProductFormService, FamilleProductFormGroup } from './famille-product-form.service';
import { IFamilleProduct } from '../famille-product.model';
import { FamilleProductService } from '../service/famille-product.service';

@Component({
  selector: 'jhi-famille-product-update',
  templateUrl: './famille-product-update.component.html',
})
export class FamilleProductUpdateComponent implements OnInit {
  isSaving = false;
  familleProduct: IFamilleProduct | null = null;

  editForm: FamilleProductFormGroup = this.familleProductFormService.createFamilleProductFormGroup();

  constructor(
    protected familleProductService: FamilleProductService,
    protected familleProductFormService: FamilleProductFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familleProduct }) => {
      this.familleProduct = familleProduct;
      if (familleProduct) {
        this.updateForm(familleProduct);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familleProduct = this.familleProductFormService.getFamilleProduct(this.editForm);
    if (familleProduct.id !== null) {
      this.subscribeToSaveResponse(this.familleProductService.update(familleProduct));
    } else {
      this.subscribeToSaveResponse(this.familleProductService.create(familleProduct));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilleProduct>>): void {
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

  protected updateForm(familleProduct: IFamilleProduct): void {
    this.familleProduct = familleProduct;
    this.familleProductFormService.resetForm(this.editForm, familleProduct);
  }
}
