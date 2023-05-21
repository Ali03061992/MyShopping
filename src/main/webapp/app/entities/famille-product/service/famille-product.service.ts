import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilleProduct, NewFamilleProduct } from '../famille-product.model';

export type PartialUpdateFamilleProduct = Partial<IFamilleProduct> & Pick<IFamilleProduct, 'id'>;

export type EntityResponseType = HttpResponse<IFamilleProduct>;
export type EntityArrayResponseType = HttpResponse<IFamilleProduct[]>;

@Injectable({ providedIn: 'root' })
export class FamilleProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/famille-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(familleProduct: NewFamilleProduct): Observable<EntityResponseType> {
    return this.http.post<IFamilleProduct>(this.resourceUrl, familleProduct, { observe: 'response' });
  }

  update(familleProduct: IFamilleProduct): Observable<EntityResponseType> {
    return this.http.put<IFamilleProduct>(`${this.resourceUrl}/${this.getFamilleProductIdentifier(familleProduct)}`, familleProduct, {
      observe: 'response',
    });
  }

  partialUpdate(familleProduct: PartialUpdateFamilleProduct): Observable<EntityResponseType> {
    return this.http.patch<IFamilleProduct>(`${this.resourceUrl}/${this.getFamilleProductIdentifier(familleProduct)}`, familleProduct, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamilleProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamilleProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFamilleProductIdentifier(familleProduct: Pick<IFamilleProduct, 'id'>): number {
    return familleProduct.id;
  }

  compareFamilleProduct(o1: Pick<IFamilleProduct, 'id'> | null, o2: Pick<IFamilleProduct, 'id'> | null): boolean {
    return o1 && o2 ? this.getFamilleProductIdentifier(o1) === this.getFamilleProductIdentifier(o2) : o1 === o2;
  }

  addFamilleProductToCollectionIfMissing<Type extends Pick<IFamilleProduct, 'id'>>(
    familleProductCollection: Type[],
    ...familleProductsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const familleProducts: Type[] = familleProductsToCheck.filter(isPresent);
    if (familleProducts.length > 0) {
      const familleProductCollectionIdentifiers = familleProductCollection.map(
        familleProductItem => this.getFamilleProductIdentifier(familleProductItem)!
      );
      const familleProductsToAdd = familleProducts.filter(familleProductItem => {
        const familleProductIdentifier = this.getFamilleProductIdentifier(familleProductItem);
        if (familleProductCollectionIdentifiers.includes(familleProductIdentifier)) {
          return false;
        }
        familleProductCollectionIdentifiers.push(familleProductIdentifier);
        return true;
      });
      return [...familleProductsToAdd, ...familleProductCollection];
    }
    return familleProductCollection;
  }
}
