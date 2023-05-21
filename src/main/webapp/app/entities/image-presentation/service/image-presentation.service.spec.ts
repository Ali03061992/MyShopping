import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImagePresentation } from '../image-presentation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../image-presentation.test-samples';

import { ImagePresentationService } from './image-presentation.service';

const requireRestSample: IImagePresentation = {
  ...sampleWithRequiredData,
};

describe('ImagePresentation Service', () => {
  let service: ImagePresentationService;
  let httpMock: HttpTestingController;
  let expectedResult: IImagePresentation | IImagePresentation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImagePresentationService);
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

    it('should create a ImagePresentation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const imagePresentation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imagePresentation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImagePresentation', () => {
      const imagePresentation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imagePresentation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImagePresentation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImagePresentation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImagePresentation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImagePresentationToCollectionIfMissing', () => {
      it('should add a ImagePresentation to an empty array', () => {
        const imagePresentation: IImagePresentation = sampleWithRequiredData;
        expectedResult = service.addImagePresentationToCollectionIfMissing([], imagePresentation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagePresentation);
      });

      it('should not add a ImagePresentation to an array that contains it', () => {
        const imagePresentation: IImagePresentation = sampleWithRequiredData;
        const imagePresentationCollection: IImagePresentation[] = [
          {
            ...imagePresentation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImagePresentationToCollectionIfMissing(imagePresentationCollection, imagePresentation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImagePresentation to an array that doesn't contain it", () => {
        const imagePresentation: IImagePresentation = sampleWithRequiredData;
        const imagePresentationCollection: IImagePresentation[] = [sampleWithPartialData];
        expectedResult = service.addImagePresentationToCollectionIfMissing(imagePresentationCollection, imagePresentation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagePresentation);
      });

      it('should add only unique ImagePresentation to an array', () => {
        const imagePresentationArray: IImagePresentation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imagePresentationCollection: IImagePresentation[] = [sampleWithRequiredData];
        expectedResult = service.addImagePresentationToCollectionIfMissing(imagePresentationCollection, ...imagePresentationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imagePresentation: IImagePresentation = sampleWithRequiredData;
        const imagePresentation2: IImagePresentation = sampleWithPartialData;
        expectedResult = service.addImagePresentationToCollectionIfMissing([], imagePresentation, imagePresentation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagePresentation);
        expect(expectedResult).toContain(imagePresentation2);
      });

      it('should accept null and undefined values', () => {
        const imagePresentation: IImagePresentation = sampleWithRequiredData;
        expectedResult = service.addImagePresentationToCollectionIfMissing([], null, imagePresentation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagePresentation);
      });

      it('should return initial array if no ImagePresentation is added', () => {
        const imagePresentationCollection: IImagePresentation[] = [sampleWithRequiredData];
        expectedResult = service.addImagePresentationToCollectionIfMissing(imagePresentationCollection, undefined, null);
        expect(expectedResult).toEqual(imagePresentationCollection);
      });
    });

    describe('compareImagePresentation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImagePresentation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareImagePresentation(entity1, entity2);
        const compareResult2 = service.compareImagePresentation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareImagePresentation(entity1, entity2);
        const compareResult2 = service.compareImagePresentation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareImagePresentation(entity1, entity2);
        const compareResult2 = service.compareImagePresentation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
