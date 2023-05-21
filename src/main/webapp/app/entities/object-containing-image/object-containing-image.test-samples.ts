import { IObjectContainingImage, NewObjectContainingImage } from './object-containing-image.model';

export const sampleWithRequiredData: IObjectContainingImage = {
  id: 5345,
};

export const sampleWithPartialData: IObjectContainingImage = {
  id: 48384,
};

export const sampleWithFullData: IObjectContainingImage = {
  id: 50480,
  name: '../fake-data/blob/hipster.png',
  nameContentType: 'unknown',
};

export const sampleWithNewData: NewObjectContainingImage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
