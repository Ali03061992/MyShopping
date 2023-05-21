import { IImagePresentation, NewImagePresentation } from './image-presentation.model';

export const sampleWithRequiredData: IImagePresentation = {
  id: 87477,
};

export const sampleWithPartialData: IImagePresentation = {
  id: 91223,
};

export const sampleWithFullData: IImagePresentation = {
  id: 38721,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewImagePresentation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
