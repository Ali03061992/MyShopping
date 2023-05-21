import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FamilleProductComponent } from './list/famille-product.component';
import { FamilleProductDetailComponent } from './detail/famille-product-detail.component';
import { FamilleProductUpdateComponent } from './update/famille-product-update.component';
import { FamilleProductDeleteDialogComponent } from './delete/famille-product-delete-dialog.component';
import { FamilleProductRoutingModule } from './route/famille-product-routing.module';

@NgModule({
  imports: [SharedModule, FamilleProductRoutingModule],
  declarations: [
    FamilleProductComponent,
    FamilleProductDetailComponent,
    FamilleProductUpdateComponent,
    FamilleProductDeleteDialogComponent,
  ],
})
export class FamilleProductModule {}
