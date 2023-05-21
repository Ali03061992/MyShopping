import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../famille-product.test-samples';

import { FamilleProductFormService } from './famille-product-form.service';

describe('FamilleProduct Form Service', () => {
  let service: FamilleProductFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FamilleProductFormService);
  });

  describe('Service methods', () => {
    describe('createFamilleProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFamilleProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IFamilleProduct should create a new form with FormGroup', () => {
        const formGroup = service.createFamilleProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getFamilleProduct', () => {
      it('should return NewFamilleProduct for default FamilleProduct initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFamilleProductFormGroup(sampleWithNewData);

        const familleProduct = service.getFamilleProduct(formGroup) as any;

        expect(familleProduct).toMatchObject(sampleWithNewData);
      });

      it('should return NewFamilleProduct for empty FamilleProduct initial value', () => {
        const formGroup = service.createFamilleProductFormGroup();

        const familleProduct = service.getFamilleProduct(formGroup) as any;

        expect(familleProduct).toMatchObject({});
      });

      it('should return IFamilleProduct', () => {
        const formGroup = service.createFamilleProductFormGroup(sampleWithRequiredData);

        const familleProduct = service.getFamilleProduct(formGroup) as any;

        expect(familleProduct).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFamilleProduct should not enable id FormControl', () => {
        const formGroup = service.createFamilleProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFamilleProduct should disable id FormControl', () => {
        const formGroup = service.createFamilleProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
