import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObjectContainingImage } from '../object-containing-image.model';
import { ObjectContainingImageService } from '../service/object-containing-image.service';

@Injectable({ providedIn: 'root' })
export class ObjectContainingImageRoutingResolveService implements Resolve<IObjectContainingImage | null> {
  constructor(protected service: ObjectContainingImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObjectContainingImage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((objectContainingImage: HttpResponse<IObjectContainingImage>) => {
          if (objectContainingImage.body) {
            return of(objectContainingImage.body);
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
