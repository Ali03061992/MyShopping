import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObjectContainingImageComponent } from '../list/object-containing-image.component';
import { ObjectContainingImageDetailComponent } from '../detail/object-containing-image-detail.component';
import { ObjectContainingImageUpdateComponent } from '../update/object-containing-image-update.component';
import { ObjectContainingImageRoutingResolveService } from './object-containing-image-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const objectContainingImageRoute: Routes = [
  {
    path: '',
    component: ObjectContainingImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObjectContainingImageDetailComponent,
    resolve: {
      objectContainingImage: ObjectContainingImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObjectContainingImageUpdateComponent,
    resolve: {
      objectContainingImage: ObjectContainingImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObjectContainingImageUpdateComponent,
    resolve: {
      objectContainingImage: ObjectContainingImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(objectContainingImageRoute)],
  exports: [RouterModule],
})
export class ObjectContainingImageRoutingModule {}
