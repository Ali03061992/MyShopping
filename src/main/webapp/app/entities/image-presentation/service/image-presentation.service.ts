import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImagePresentation, NewImagePresentation } from '../image-presentation.model';

export type PartialUpdateImagePresentation = Partial<IImagePresentation> & Pick<IImagePresentation, 'id'>;

export type EntityResponseType = HttpResponse<IImagePresentation>;
export type EntityArrayResponseType = HttpResponse<IImagePresentation[]>;

@Injectable({ providedIn: 'root' })
export class ImagePresentationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-presentations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imagePresentation: NewImagePresentation): Observable<EntityResponseType> {
    return this.http.post<IImagePresentation>(this.resourceUrl, imagePresentation, { observe: 'response' });
  }

  update(imagePresentation: IImagePresentation): Observable<EntityResponseType> {
    return this.http.put<IImagePresentation>(
      `${this.resourceUrl}/${this.getImagePresentationIdentifier(imagePresentation)}`,
      imagePresentation,
      { observe: 'response' }
    );
  }

  partialUpdate(imagePresentation: PartialUpdateImagePresentation): Observable<EntityResponseType> {
    return this.http.patch<IImagePresentation>(
      `${this.resourceUrl}/${this.getImagePresentationIdentifier(imagePresentation)}`,
      imagePresentation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImagePresentation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImagePresentation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImagePresentationIdentifier(imagePresentation: Pick<IImagePresentation, 'id'>): number {
    return imagePresentation.id;
  }

  compareImagePresentation(o1: Pick<IImagePresentation, 'id'> | null, o2: Pick<IImagePresentation, 'id'> | null): boolean {
    return o1 && o2 ? this.getImagePresentationIdentifier(o1) === this.getImagePresentationIdentifier(o2) : o1 === o2;
  }

  addImagePresentationToCollectionIfMissing<Type extends Pick<IImagePresentation, 'id'>>(
    imagePresentationCollection: Type[],
    ...imagePresentationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imagePresentations: Type[] = imagePresentationsToCheck.filter(isPresent);
    if (imagePresentations.length > 0) {
      const imagePresentationCollectionIdentifiers = imagePresentationCollection.map(
        imagePresentationItem => this.getImagePresentationIdentifier(imagePresentationItem)!
      );
      const imagePresentationsToAdd = imagePresentations.filter(imagePresentationItem => {
        const imagePresentationIdentifier = this.getImagePresentationIdentifier(imagePresentationItem);
        if (imagePresentationCollectionIdentifiers.includes(imagePresentationIdentifier)) {
          return false;
        }
        imagePresentationCollectionIdentifiers.push(imagePresentationIdentifier);
        return true;
      });
      return [...imagePresentationsToAdd, ...imagePresentationCollection];
    }
    return imagePresentationCollection;
  }
}
