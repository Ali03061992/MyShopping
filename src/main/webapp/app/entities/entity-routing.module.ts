import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        data: { pageTitle: 'myShoppingApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'object-containing-image',
        data: { pageTitle: 'myShoppingApp.objectContainingImage.home.title' },
        loadChildren: () => import('./object-containing-image/object-containing-image.module').then(m => m.ObjectContainingImageModule),
      },
      {
        path: 'product-category',
        data: { pageTitle: 'myShoppingApp.productCategory.home.title' },
        loadChildren: () => import('./product-category/product-category.module').then(m => m.ProductCategoryModule),
      },
      {
        path: 'image-presentation',
        data: { pageTitle: 'myShoppingApp.imagePresentation.home.title' },
        loadChildren: () => import('./image-presentation/image-presentation.module').then(m => m.ImagePresentationModule),
      },
      {
        path: 'famille-product',
        data: { pageTitle: 'myShoppingApp.familleProduct.home.title' },
        loadChildren: () => import('./famille-product/famille-product.module').then(m => m.FamilleProductModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
