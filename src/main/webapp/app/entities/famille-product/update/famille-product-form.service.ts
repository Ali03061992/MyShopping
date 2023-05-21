import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFamilleProduct, NewFamilleProduct } from '../famille-product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFamilleProduct for edit and NewFamilleProductFormGroupInput for create.
 */
type FamilleProductFormGroupInput = IFamilleProduct | PartialWithRequiredKeyOf<NewFamilleProduct>;

type FamilleProductFormDefaults = Pick<NewFamilleProduct, 'id'>;

type FamilleProductFormGroupContent = {
  id: FormControl<IFamilleProduct['id'] | NewFamilleProduct['id']>;
  name: FormControl<IFamilleProduct['name']>;
  type: FormControl<IFamilleProduct['type']>;
};

export type FamilleProductFormGroup = FormGroup<FamilleProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FamilleProductFormService {
  createFamilleProductFormGroup(familleProduct: FamilleProductFormGroupInput = { id: null }): FamilleProductFormGroup {
    const familleProductRawValue = {
      ...this.getFormDefaults(),
      ...familleProduct,
    };
    return new FormGroup<FamilleProductFormGroupContent>({
      id: new FormControl(
        { value: familleProductRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(familleProductRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(familleProductRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getFamilleProduct(form: FamilleProductFormGroup): IFamilleProduct | NewFamilleProduct {
    return form.getRawValue() as IFamilleProduct | NewFamilleProduct;
  }

  resetForm(form: FamilleProductFormGroup, familleProduct: FamilleProductFormGroupInput): void {
    const familleProductRawValue = { ...this.getFormDefaults(), ...familleProduct };
    form.reset(
      {
        ...familleProductRawValue,
        id: { value: familleProductRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FamilleProductFormDefaults {
    return {
      id: null,
    };
  }
}
