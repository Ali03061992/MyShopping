import { IProductCategory } from 'app/entities/product-category/product-category.model';

export interface IProduct {
  id: number;
  productName?: string | null;
  productPrice?: number | null;
  productCategories?: Pick<IProductCategory, 'id' | 'categoryName'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
