import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFamilleProduct } from '../famille-product.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../famille-product.test-samples';

import { FamilleProductService } from './famille-product.service';

const requireRestSample: IFamilleProduct = {
  ...sampleWithRequiredData,
};

describe('FamilleProduct Service', () => {
  let service: FamilleProductService;
  let httpMock: HttpTestingController;
  let expectedResult: IFamilleProduct | IFamilleProduct[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FamilleProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FamilleProduct', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const familleProduct = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(familleProduct).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FamilleProduct', () => {
      const familleProduct = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(familleProduct).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FamilleProduct', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FamilleProduct', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FamilleProduct', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFamilleProductToCollectionIfMissing', () => {
      it('should add a FamilleProduct to an empty array', () => {
        const familleProduct: IFamilleProduct = sampleWithRequiredData;
        expectedResult = service.addFamilleProductToCollectionIfMissing([], familleProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familleProduct);
      });

      it('should not add a FamilleProduct to an array that contains it', () => {
        const familleProduct: IFamilleProduct = sampleWithRequiredData;
        const familleProductCollection: IFamilleProduct[] = [
          {
            ...familleProduct,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFamilleProductToCollectionIfMissing(familleProductCollection, familleProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FamilleProduct to an array that doesn't contain it", () => {
        const familleProduct: IFamilleProduct = sampleWithRequiredData;
        const familleProductCollection: IFamilleProduct[] = [sampleWithPartialData];
        expectedResult = service.addFamilleProductToCollectionIfMissing(familleProductCollection, familleProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familleProduct);
      });

      it('should add only unique FamilleProduct to an array', () => {
        const familleProductArray: IFamilleProduct[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const familleProductCollection: IFamilleProduct[] = [sampleWithRequiredData];
        expectedResult = service.addFamilleProductToCollectionIfMissing(familleProductCollection, ...familleProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const familleProduct: IFamilleProduct = sampleWithRequiredData;
        const familleProduct2: IFamilleProduct = sampleWithPartialData;
        expectedResult = service.addFamilleProductToCollectionIfMissing([], familleProduct, familleProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familleProduct);
        expect(expectedResult).toContain(familleProduct2);
      });

      it('should accept null and undefined values', () => {
        const familleProduct: IFamilleProduct = sampleWithRequiredData;
        expectedResult = service.addFamilleProductToCollectionIfMissing([], null, familleProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familleProduct);
      });

      it('should return initial array if no FamilleProduct is added', () => {
        const familleProductCollection: IFamilleProduct[] = [sampleWithRequiredData];
        expectedResult = service.addFamilleProductToCollectionIfMissing(familleProductCollection, undefined, null);
        expect(expectedResult).toEqual(familleProductCollection);
      });
    });

    describe('compareFamilleProduct', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFamilleProduct(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFamilleProduct(entity1, entity2);
        const compareResult2 = service.compareFamilleProduct(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFamilleProduct(entity1, entity2);
        const compareResult2 = service.compareFamilleProduct(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFamilleProduct(entity1, entity2);
        const compareResult2 = service.compareFamilleProduct(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
