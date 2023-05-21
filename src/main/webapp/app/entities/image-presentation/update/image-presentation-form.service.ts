import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IImagePresentation, NewImagePresentation } from '../image-presentation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImagePresentation for edit and NewImagePresentationFormGroupInput for create.
 */
type ImagePresentationFormGroupInput = IImagePresentation | PartialWithRequiredKeyOf<NewImagePresentation>;

type ImagePresentationFormDefaults = Pick<NewImagePresentation, 'id'>;

type ImagePresentationFormGroupContent = {
  id: FormControl<IImagePresentation['id'] | NewImagePresentation['id']>;
  image: FormControl<IImagePresentation['image']>;
  imageContentType: FormControl<IImagePresentation['imageContentType']>;
  imagesPresentation: FormControl<IImagePresentation['imagesPresentation']>;
};

export type ImagePresentationFormGroup = FormGroup<ImagePresentationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImagePresentationFormService {
  createImagePresentationFormGroup(imagePresentation: ImagePresentationFormGroupInput = { id: null }): ImagePresentationFormGroup {
    const imagePresentationRawValue = {
      ...this.getFormDefaults(),
      ...imagePresentation,
    };
    return new FormGroup<ImagePresentationFormGroupContent>({
      id: new FormControl(
        { value: imagePresentationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      image: new FormControl(imagePresentationRawValue.image),
      imageContentType: new FormControl(imagePresentationRawValue.imageContentType),
      imagesPresentation: new FormControl(imagePresentationRawValue.imagesPresentation),
    });
  }

  getImagePresentation(form: ImagePresentationFormGroup): IImagePresentation | NewImagePresentation {
    return form.getRawValue() as IImagePresentation | NewImagePresentation;
  }

  resetForm(form: ImagePresentationFormGroup, imagePresentation: ImagePresentationFormGroupInput): void {
    const imagePresentationRawValue = { ...this.getFormDefaults(), ...imagePresentation };
    form.reset(
      {
        ...imagePresentationRawValue,
        id: { value: imagePresentationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImagePresentationFormDefaults {
    return {
      id: null,
    };
  }
}
