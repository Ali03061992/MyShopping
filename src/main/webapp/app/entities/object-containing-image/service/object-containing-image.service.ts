import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IObjectContainingImage, NewObjectContainingImage } from '../object-containing-image.model';

export type PartialUpdateObjectContainingImage = Partial<IObjectContainingImage> & Pick<IObjectContainingImage, 'id'>;

export type EntityResponseType = HttpResponse<IObjectContainingImage>;
export type EntityArrayResponseType = HttpResponse<IObjectContainingImage[]>;

@Injectable({ providedIn: 'root' })
export class ObjectContainingImageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/object-containing-images');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(objectContainingImage: NewObjectContainingImage): Observable<EntityResponseType> {
    return this.http.post<IObjectContainingImage>(this.resourceUrl, objectContainingImage, { observe: 'response' });
  }

  update(objectContainingImage: IObjectContainingImage): Observable<EntityResponseType> {
    return this.http.put<IObjectContainingImage>(
      `${this.resourceUrl}/${this.getObjectContainingImageIdentifier(objectContainingImage)}`,
      objectContainingImage,
      { observe: 'response' }
    );
  }

  partialUpdate(objectContainingImage: PartialUpdateObjectContainingImage): Observable<EntityResponseType> {
    return this.http.patch<IObjectContainingImage>(
      `${this.resourceUrl}/${this.getObjectContainingImageIdentifier(objectContainingImage)}`,
      objectContainingImage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IObjectContainingImage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IObjectContainingImage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getObjectContainingImageIdentifier(objectContainingImage: Pick<IObjectContainingImage, 'id'>): number {
    return objectContainingImage.id;
  }

  compareObjectContainingImage(o1: Pick<IObjectContainingImage, 'id'> | null, o2: Pick<IObjectContainingImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getObjectContainingImageIdentifier(o1) === this.getObjectContainingImageIdentifier(o2) : o1 === o2;
  }

  addObjectContainingImageToCollectionIfMissing<Type extends Pick<IObjectContainingImage, 'id'>>(
    objectContainingImageCollection: Type[],
    ...objectContainingImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const objectContainingImages: Type[] = objectContainingImagesToCheck.filter(isPresent);
    if (objectContainingImages.length > 0) {
      const objectContainingImageCollectionIdentifiers = objectContainingImageCollection.map(
        objectContainingImageItem => this.getObjectContainingImageIdentifier(objectContainingImageItem)!
      );
      const objectContainingImagesToAdd = objectContainingImages.filter(objectContainingImageItem => {
        const objectContainingImageIdentifier = this.getObjectContainingImageIdentifier(objectContainingImageItem);
        if (objectContainingImageCollectionIdentifiers.includes(objectContainingImageIdentifier)) {
          return false;
        }
        objectContainingImageCollectionIdentifiers.push(objectContainingImageIdentifier);
        return true;
      });
      return [...objectContainingImagesToAdd, ...objectContainingImageCollection];
    }
    return objectContainingImageCollection;
  }
}
