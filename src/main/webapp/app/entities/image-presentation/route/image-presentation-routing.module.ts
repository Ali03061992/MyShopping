import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImagePresentationComponent } from '../list/image-presentation.component';
import { ImagePresentationDetailComponent } from '../detail/image-presentation-detail.component';
import { ImagePresentationUpdateComponent } from '../update/image-presentation-update.component';
import { ImagePresentationRoutingResolveService } from './image-presentation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const imagePresentationRoute: Routes = [
  {
    path: '',
    component: ImagePresentationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImagePresentationDetailComponent,
    resolve: {
      imagePresentation: ImagePresentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImagePresentationUpdateComponent,
    resolve: {
      imagePresentation: ImagePresentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImagePresentationUpdateComponent,
    resolve: {
      imagePresentation: ImagePresentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imagePresentationRoute)],
  exports: [RouterModule],
})
export class ImagePresentationRoutingModule {}
