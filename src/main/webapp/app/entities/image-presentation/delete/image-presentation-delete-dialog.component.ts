import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImagePresentation } from '../image-presentation.model';
import { ImagePresentationService } from '../service/image-presentation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './image-presentation-delete-dialog.component.html',
})
export class ImagePresentationDeleteDialogComponent {
  imagePresentation?: IImagePresentation;

  constructor(protected imagePresentationService: ImagePresentationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imagePresentationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
