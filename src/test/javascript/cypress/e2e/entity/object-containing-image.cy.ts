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

describe('ObjectContainingImage e2e test', () => {
  const objectContainingImagePageUrl = '/object-containing-image';
  const objectContainingImagePageUrlPattern = new RegExp('/object-containing-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const objectContainingImageSample = { name: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=', nameContentType: 'unknown' };

  let objectContainingImage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/object-containing-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/object-containing-images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/object-containing-images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (objectContainingImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/object-containing-images/${objectContainingImage.id}`,
      }).then(() => {
        objectContainingImage = undefined;
      });
    }
  });

  it('ObjectContainingImages menu should load ObjectContainingImages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('object-containing-image');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ObjectContainingImage').should('exist');
    cy.url().should('match', objectContainingImagePageUrlPattern);
  });

  describe('ObjectContainingImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(objectContainingImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ObjectContainingImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/object-containing-image/new$'));
        cy.getEntityCreateUpdateHeading('ObjectContainingImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', objectContainingImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/object-containing-images',
          body: objectContainingImageSample,
        }).then(({ body }) => {
          objectContainingImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/object-containing-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [objectContainingImage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(objectContainingImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ObjectContainingImage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('objectContainingImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', objectContainingImagePageUrlPattern);
      });

      it('edit button click should load edit ObjectContainingImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ObjectContainingImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', objectContainingImagePageUrlPattern);
      });

      it('edit button click should load edit ObjectContainingImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ObjectContainingImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', objectContainingImagePageUrlPattern);
      });

      it('last delete button click should delete instance of ObjectContainingImage', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('objectContainingImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', objectContainingImagePageUrlPattern);

        objectContainingImage = undefined;
      });
    });
  });

  describe('new ObjectContainingImage page', () => {
    beforeEach(() => {
      cy.visit(`${objectContainingImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ObjectContainingImage');
    });

    it('should create an instance of ObjectContainingImage', () => {
      cy.setFieldImageAsBytesOfEntity('name', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        objectContainingImage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', objectContainingImagePageUrlPattern);
    });
  });
});
