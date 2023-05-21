import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImagePresentationComponent } from './list/image-presentation.component';
import { ImagePresentationDetailComponent } from './detail/image-presentation-detail.component';
import { ImagePresentationUpdateComponent } from './update/image-presentation-update.component';
import { ImagePresentationDeleteDialogComponent } from './delete/image-presentation-delete-dialog.component';
import { ImagePresentationRoutingModule } from './route/image-presentation-routing.module';

@NgModule({
  imports: [SharedModule, ImagePresentationRoutingModule],
  declarations: [
    ImagePresentationComponent,
    ImagePresentationDetailComponent,
    ImagePresentationUpdateComponent,
    ImagePresentationDeleteDialogComponent,
  ],
})
export class ImagePresentationModule {}
