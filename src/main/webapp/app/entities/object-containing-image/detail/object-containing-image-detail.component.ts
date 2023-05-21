import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObjectContainingImage } from '../object-containing-image.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-object-containing-image-detail',
  templateUrl: './object-containing-image-detail.component.html',
})
export class ObjectContainingImageDetailComponent implements OnInit {
  objectContainingImage: IObjectContainingImage | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objectContainingImage }) => {
      this.objectContainingImage = objectContainingImage;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
