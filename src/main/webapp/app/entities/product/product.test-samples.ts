import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 77672,
  productName: 'Customer-focused',
  productPrice: 96307,
};

export const sampleWithPartialData: IProduct = {
  id: 78560,
  productName: 'a Programmable',
  productPrice: 22539,
};

export const sampleWithFullData: IProduct = {
  id: 49054,
  productName: 'deliver',
  productPrice: 39641,
};

export const sampleWithNewData: NewProduct = {
  productName: 'models Saint-Dominique invoice',
  productPrice: 80148,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
