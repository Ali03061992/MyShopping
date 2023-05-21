import { IProduct } from 'app/entities/product/product.model';

export interface IProductCategory {
  id: number;
  categoryName?: string | null;
  productNames?: Pick<IProduct, 'id'>[] | null;
}

export type NewProductCategory = Omit<IProductCategory, 'id'> & { id: null };
