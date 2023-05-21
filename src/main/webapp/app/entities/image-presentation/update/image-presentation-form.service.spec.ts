import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../image-presentation.test-samples';

import { ImagePresentationFormService } from './image-presentation-form.service';

describe('ImagePresentation Form Service', () => {
  let service: ImagePresentationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImagePresentationFormService);
  });

  describe('Service methods', () => {
    describe('createImagePresentationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImagePresentationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            image: expect.any(Object),
            imagesPresentation: expect.any(Object),
          })
        );
      });

      it('passing IImagePresentation should create a new form with FormGroup', () => {
        const formGroup = service.createImagePresentationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            image: expect.any(Object),
            imagesPresentation: expect.any(Object),
          })
        );
      });
    });

    describe('getImagePresentation', () => {
      it('should return NewImagePresentation for default ImagePresentation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createImagePresentationFormGroup(sampleWithNewData);

        const imagePresentation = service.getImagePresentation(formGroup) as any;

        expect(imagePresentation).toMatchObject(sampleWithNewData);
      });

      it('should return NewImagePresentation for empty ImagePresentation initial value', () => {
        const formGroup = service.createImagePresentationFormGroup();

        const imagePresentation = service.getImagePresentation(formGroup) as any;

        expect(imagePresentation).toMatchObject({});
      });

      it('should return IImagePresentation', () => {
        const formGroup = service.createImagePresentationFormGroup(sampleWithRequiredData);

        const imagePresentation = service.getImagePresentation(formGroup) as any;

        expect(imagePresentation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImagePresentation should not enable id FormControl', () => {
        const formGroup = service.createImagePresentationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImagePresentation should disable id FormControl', () => {
        const formGroup = service.createImagePresentationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
