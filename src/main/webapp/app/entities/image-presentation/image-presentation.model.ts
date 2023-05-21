import { IProduct } from 'app/entities/product/product.model';

export interface IImagePresentation {
  id: number;
  image?: string | null;
  imageContentType?: string | null;
  imagesPresentation?: Pick<IProduct, 'id'> | null;
}

export type NewImagePresentation = Omit<IImagePresentation, 'id'> & { id: null };
