import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IObjectContainingImage } from '../object-containing-image.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../object-containing-image.test-samples';

import { ObjectContainingImageService } from './object-containing-image.service';

const requireRestSample: IObjectContainingImage = {
  ...sampleWithRequiredData,
};

describe('ObjectContainingImage Service', () => {
  let service: ObjectContainingImageService;
  let httpMock: HttpTestingController;
  let expectedResult: IObjectContainingImage | IObjectContainingImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ObjectContainingImageService);
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

    it('should create a ObjectContainingImage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const objectContainingImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(objectContainingImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ObjectContainingImage', () => {
      const objectContainingImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(objectContainingImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ObjectContainingImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ObjectContainingImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ObjectContainingImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addObjectContainingImageToCollectionIfMissing', () => {
      it('should add a ObjectContainingImage to an empty array', () => {
        const objectContainingImage: IObjectContainingImage = sampleWithRequiredData;
        expectedResult = service.addObjectContainingImageToCollectionIfMissing([], objectContainingImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objectContainingImage);
      });

      it('should not add a ObjectContainingImage to an array that contains it', () => {
        const objectContainingImage: IObjectContainingImage = sampleWithRequiredData;
        const objectContainingImageCollection: IObjectContainingImage[] = [
          {
            ...objectContainingImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addObjectContainingImageToCollectionIfMissing(objectContainingImageCollection, objectContainingImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ObjectContainingImage to an array that doesn't contain it", () => {
        const objectContainingImage: IObjectContainingImage = sampleWithRequiredData;
        const objectContainingImageCollection: IObjectContainingImage[] = [sampleWithPartialData];
        expectedResult = service.addObjectContainingImageToCollectionIfMissing(objectContainingImageCollection, objectContainingImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objectContainingImage);
      });

      it('should add only unique ObjectContainingImage to an array', () => {
        const objectContainingImageArray: IObjectContainingImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const objectContainingImageCollection: IObjectContainingImage[] = [sampleWithRequiredData];
        expectedResult = service.addObjectContainingImageToCollectionIfMissing(
          objectContainingImageCollection,
          ...objectContainingImageArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const objectContainingImage: IObjectContainingImage = sampleWithRequiredData;
        const objectContainingImage2: IObjectContainingImage = sampleWithPartialData;
        expectedResult = service.addObjectContainingImageToCollectionIfMissing([], objectContainingImage, objectContainingImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objectContainingImage);
        expect(expectedResult).toContain(objectContainingImage2);
      });

      it('should accept null and undefined values', () => {
        const objectContainingImage: IObjectContainingImage = sampleWithRequiredData;
        expectedResult = service.addObjectContainingImageToCollectionIfMissing([], null, objectContainingImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objectContainingImage);
      });

      it('should return initial array if no ObjectContainingImage is added', () => {
        const objectContainingImageCollection: IObjectContainingImage[] = [sampleWithRequiredData];
        expectedResult = service.addObjectContainingImageToCollectionIfMissing(objectContainingImageCollection, undefined, null);
        expect(expectedResult).toEqual(objectContainingImageCollection);
      });
    });

    describe('compareObjectContainingImage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareObjectContainingImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareObjectContainingImage(entity1, entity2);
        const compareResult2 = service.compareObjectContainingImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareObjectContainingImage(entity1, entity2);
        const compareResult2 = service.compareObjectContainingImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareObjectContainingImage(entity1, entity2);
        const compareResult2 = service.compareObjectContainingImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
