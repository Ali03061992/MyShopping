import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ImagePresentation e2e test', () => {
  const imagePresentationPageUrl = '/image-presentation';
  const imagePresentationPageUrlPattern = new RegExp('/image-presentation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const imagePresentationSample = { image: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=', imageContentType: 'unknown' };

  let imagePresentation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/image-presentations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/image-presentations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/image-presentations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (imagePresentation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/image-presentations/${imagePresentation.id}`,
      }).then(() => {
        imagePresentation = undefined;
      });
    }
  });

  it('ImagePresentations menu should load ImagePresentations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('image-presentation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ImagePresentation').should('exist');
    cy.url().should('match', imagePresentationPageUrlPattern);
  });

  describe('ImagePresentation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(imagePresentationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ImagePresentation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/image-presentation/new$'));
        cy.getEntityCreateUpdateHeading('ImagePresentation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagePresentationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/image-presentations',
          body: imagePresentationSample,
        }).then(({ body }) => {
          imagePresentation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/image-presentations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [imagePresentation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(imagePresentationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ImagePresentation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('imagePresentation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagePresentationPageUrlPattern);
      });

      it('edit button click should load edit ImagePresentation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImagePresentation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagePresentationPageUrlPattern);
      });

      it('edit button click should load edit ImagePresentation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImagePresentation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagePresentationPageUrlPattern);
      });

      it('last delete button click should delete instance of ImagePresentation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('imagePresentation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagePresentationPageUrlPattern);

        imagePresentation = undefined;
      });
    });
  });

  describe('new ImagePresentation page', () => {
    beforeEach(() => {
      cy.visit(`${imagePresentationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ImagePresentation');
    });

    it('should create an instance of ImagePresentation', () => {
      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        imagePresentation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', imagePresentationPageUrlPattern);
    });
  });
});
