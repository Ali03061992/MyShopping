import { IProductCategory, NewProductCategory } from './product-category.model';

export const sampleWithRequiredData: IProductCategory = {
  id: 39228,
  categoryName: 'invoice Guinée',
};

export const sampleWithPartialData: IProductCategory = {
  id: 18292,
  categoryName: 'matrix Seamless Fresh',
};

export const sampleWithFullData: IProductCategory = {
  id: 30193,
  categoryName: 'Soft',
};

export const sampleWithNewData: NewProductCategory = {
  categoryName: 'neural tolerance Borders',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
