import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductCategory, NewProductCategory } from '../product-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductCategory for edit and NewProductCategoryFormGroupInput for create.
 */
type ProductCategoryFormGroupInput = IProductCategory | PartialWithRequiredKeyOf<NewProductCategory>;

type ProductCategoryFormDefaults = Pick<NewProductCategory, 'id' | 'productNames'>;

type ProductCategoryFormGroupContent = {
  id: FormControl<IProductCategory['id'] | NewProductCategory['id']>;
  categoryName: FormControl<IProductCategory['categoryName']>;
  productNames: FormControl<IProductCategory['productNames']>;
};

export type ProductCategoryFormGroup = FormGroup<ProductCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductCategoryFormService {
  createProductCategoryFormGroup(productCategory: ProductCategoryFormGroupInput = { id: null }): ProductCategoryFormGroup {
    const productCategoryRawValue = {
      ...this.getFormDefaults(),
      ...productCategory,
    };
    return new FormGroup<ProductCategoryFormGroupContent>({
      id: new FormControl(
        { value: productCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      categoryName: new FormControl(productCategoryRawValue.categoryName, {
        validators: [Validators.required],
      }),
      productNames: new FormControl(productCategoryRawValue.productNames ?? []),
    });
  }

  getProductCategory(form: ProductCategoryFormGroup): IProductCategory | NewProductCategory {
    return form.getRawValue() as IProductCategory | NewProductCategory;
  }

  resetForm(form: ProductCategoryFormGroup, productCategory: ProductCategoryFormGroupInput): void {
    const productCategoryRawValue = { ...this.getFormDefaults(), ...productCategory };
    form.reset(
      {
        ...productCategoryRawValue,
        id: { value: productCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductCategoryFormDefaults {
    return {
      id: null,
      productNames: [],
    };
  }
}
