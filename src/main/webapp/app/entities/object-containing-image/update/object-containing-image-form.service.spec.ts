import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../object-containing-image.test-samples';

import { ObjectContainingImageFormService } from './object-containing-image-form.service';

describe('ObjectContainingImage Form Service', () => {
  let service: ObjectContainingImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObjectContainingImageFormService);
  });

  describe('Service methods', () => {
    describe('createObjectContainingImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createObjectContainingImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            json: expect.any(Object),
          })
        );
      });

      it('passing IObjectContainingImage should create a new form with FormGroup', () => {
        const formGroup = service.createObjectContainingImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            json: expect.any(Object),
          })
        );
      });
    });

    describe('getObjectContainingImage', () => {
      it('should return NewObjectContainingImage for default ObjectContainingImage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createObjectContainingImageFormGroup(sampleWithNewData);

        const objectContainingImage = service.getObjectContainingImage(formGroup) as any;

        expect(objectContainingImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewObjectContainingImage for empty ObjectContainingImage initial value', () => {
        const formGroup = service.createObjectContainingImageFormGroup();

        const objectContainingImage = service.getObjectContainingImage(formGroup) as any;

        expect(objectContainingImage).toMatchObject({});
      });

      it('should return IObjectContainingImage', () => {
        const formGroup = service.createObjectContainingImageFormGroup(sampleWithRequiredData);

        const objectContainingImage = service.getObjectContainingImage(formGroup) as any;

        expect(objectContainingImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IObjectContainingImage should not enable id FormControl', () => {
        const formGroup = service.createObjectContainingImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewObjectContainingImage should disable id FormControl', () => {
        const formGroup = service.createObjectContainingImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
