import { IProduct } from 'app/entities/product/product.model';

export interface IObjectContainingImage {
  id: number;
  name?: string | null;
  nameContentType?: string | null;
  json?: Pick<IProduct, 'id'> | null;
}

export type NewObjectContainingImage = Omit<IObjectContainingImage, 'id'> & { id: null };
