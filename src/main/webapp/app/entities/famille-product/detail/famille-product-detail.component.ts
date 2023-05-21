import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilleProduct } from '../famille-product.model';

@Component({
  selector: 'jhi-famille-product-detail',
  templateUrl: './famille-product-detail.component.html',
})
export class FamilleProductDetailComponent implements OnInit {
  familleProduct: IFamilleProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familleProduct }) => {
      this.familleProduct = familleProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
