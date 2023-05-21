import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImagePresentation } from '../image-presentation.model';
import { ImagePresentationService } from '../service/image-presentation.service';

@Injectable({ providedIn: 'root' })
export class ImagePresentationRoutingResolveService implements Resolve<IImagePresentation | null> {
  constructor(protected service: ImagePresentationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImagePresentation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imagePresentation: HttpResponse<IImagePresentation>) => {
          if (imagePresentation.body) {
            return of(imagePresentation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
