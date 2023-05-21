import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ObjectContainingImageComponent } from './list/object-containing-image.component';
import { ObjectContainingImageDetailComponent } from './detail/object-containing-image-detail.component';
import { ObjectContainingImageUpdateComponent } from './update/object-containing-image-update.component';
import { ObjectContainingImageDeleteDialogComponent } from './delete/object-containing-image-delete-dialog.component';
import { ObjectContainingImageRoutingModule } from './route/object-containing-image-routing.module';

@NgModule({
  imports: [SharedModule, ObjectContainingImageRoutingModule],
  declarations: [
    ObjectContainingImageComponent,
    ObjectContainingImageDetailComponent,
    ObjectContainingImageUpdateComponent,
    ObjectContainingImageDeleteDialogComponent,
  ],
})
export class ObjectContainingImageModule {}
