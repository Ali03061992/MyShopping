import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjectContainingImage } from '../object-containing-image.model';
import { ObjectContainingImageService } from '../service/object-containing-image.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './object-containing-image-delete-dialog.component.html',
})
export class ObjectContainingImageDeleteDialogComponent {
  objectContainingImage?: IObjectContainingImage;

  constructor(protected objectContainingImageService: ObjectContainingImageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.objectContainingImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
