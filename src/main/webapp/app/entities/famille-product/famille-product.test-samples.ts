import { IFamilleProduct, NewFamilleProduct } from './famille-product.model';

export const sampleWithRequiredData: IFamilleProduct = {
  id: 51603,
  name: 'transmitting deposit Concrete',
  type: 'withdrawal Credit a',
};

export const sampleWithPartialData: IFamilleProduct = {
  id: 23889,
  name: 'Cheese Ingenieur',
  type: 'SSL Saint-Séverin',
};

export const sampleWithFullData: IFamilleProduct = {
  id: 71844,
  name: 'payment',
  type: 'Analyste brand Cambridgeshire',
};

export const sampleWithNewData: NewFamilleProduct = {
  name: 'Industrial Borders',
  type: 'Customizable Dauphine facilitate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
