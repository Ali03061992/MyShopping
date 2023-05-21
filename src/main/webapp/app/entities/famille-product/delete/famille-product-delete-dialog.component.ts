import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamilleProduct } from '../famille-product.model';
import { FamilleProductService } from '../service/famille-product.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './famille-product-delete-dialog.component.html',
})
export class FamilleProductDeleteDialogComponent {
  familleProduct?: IFamilleProduct;

  constructor(protected familleProductService: FamilleProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familleProductService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
