import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IObjectContainingImage, NewObjectContainingImage } from '../object-containing-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IObjectContainingImage for edit and NewObjectContainingImageFormGroupInput for create.
 */
type ObjectContainingImageFormGroupInput = IObjectContainingImage | PartialWithRequiredKeyOf<NewObjectContainingImage>;

type ObjectContainingImageFormDefaults = Pick<NewObjectContainingImage, 'id'>;

type ObjectContainingImageFormGroupContent = {
  id: FormControl<IObjectContainingImage['id'] | NewObjectContainingImage['id']>;
  name: FormControl<IObjectContainingImage['name']>;
  nameContentType: FormControl<IObjectContainingImage['nameContentType']>;
  json: FormControl<IObjectContainingImage['json']>;
};

export type ObjectContainingImageFormGroup = FormGroup<ObjectContainingImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ObjectContainingImageFormService {
  createObjectContainingImageFormGroup(
    objectContainingImage: ObjectContainingImageFormGroupInput = { id: null }
  ): ObjectContainingImageFormGroup {
    const objectContainingImageRawValue = {
      ...this.getFormDefaults(),
      ...objectContainingImage,
    };
    return new FormGroup<ObjectContainingImageFormGroupContent>({
      id: new FormControl(
        { value: objectContainingImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(objectContainingImageRawValue.name),
      nameContentType: new FormControl(objectContainingImageRawValue.nameContentType),
      json: new FormControl(objectContainingImageRawValue.json),
    });
  }

  getObjectContainingImage(form: ObjectContainingImageFormGroup): IObjectContainingImage | NewObjectContainingImage {
    return form.getRawValue() as IObjectContainingImage | NewObjectContainingImage;
  }

  resetForm(form: ObjectContainingImageFormGroup, objectContainingImage: ObjectContainingImageFormGroupInput): void {
    const objectContainingImageRawValue = { ...this.getFormDefaults(), ...objectContainingImage };
    form.reset(
      {
        ...objectContainingImageRawValue,
        id: { value: objectContainingImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ObjectContainingImageFormDefaults {
    return {
      id: null,
    };
  }
}
