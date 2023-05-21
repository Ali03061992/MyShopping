import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FamilleProductComponent } from '../list/famille-product.component';
import { FamilleProductDetailComponent } from '../detail/famille-product-detail.component';
import { FamilleProductUpdateComponent } from '../update/famille-product-update.component';
import { FamilleProductRoutingResolveService } from './famille-product-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const familleProductRoute: Routes = [
  {
    path: '',
    component: FamilleProductComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FamilleProductDetailComponent,
    resolve: {
      familleProduct: FamilleProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FamilleProductUpdateComponent,
    resolve: {
      familleProduct: FamilleProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FamilleProductUpdateComponent,
    resolve: {
      familleProduct: FamilleProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(familleProductRoute)],
  exports: [RouterModule],
})
export class FamilleProductRoutingModule {}
