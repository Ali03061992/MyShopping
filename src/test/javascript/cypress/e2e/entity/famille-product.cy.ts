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

describe('FamilleProduct e2e test', () => {
  const familleProductPageUrl = '/famille-product';
  const familleProductPageUrlPattern = new RegExp('/famille-product(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const familleProductSample = { name: 'Checking Digitized Cross-platform', type: 'info-mediaries Handcrafted' };

  let familleProduct;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/famille-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/famille-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/famille-products/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (familleProduct) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/famille-products/${familleProduct.id}`,
      }).then(() => {
        familleProduct = undefined;
      });
    }
  });

  it('FamilleProducts menu should load FamilleProducts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('famille-product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FamilleProduct').should('exist');
    cy.url().should('match', familleProductPageUrlPattern);
  });

  describe('FamilleProduct page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(familleProductPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FamilleProduct page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/famille-product/new$'));
        cy.getEntityCreateUpdateHeading('FamilleProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familleProductPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/famille-products',
          body: familleProductSample,
        }).then(({ body }) => {
          familleProduct = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/famille-products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [familleProduct],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(familleProductPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FamilleProduct page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('familleProduct');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familleProductPageUrlPattern);
      });

      it('edit button click should load edit FamilleProduct page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FamilleProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familleProductPageUrlPattern);
      });

      it('edit button click should load edit FamilleProduct page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FamilleProduct');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familleProductPageUrlPattern);
      });

      it('last delete button click should delete instance of FamilleProduct', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('familleProduct').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familleProductPageUrlPattern);

        familleProduct = undefined;
      });
    });
  });

  describe('new FamilleProduct page', () => {
    beforeEach(() => {
      cy.visit(`${familleProductPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FamilleProduct');
    });

    it('should create an instance of FamilleProduct', () => {
      cy.get(`[data-cy="name"]`).type('IB a Sausages').should('have.value', 'IB a Sausages');

      cy.get(`[data-cy="type"]`).type('Nord-Pas-de-Calais').should('have.value', 'Nord-Pas-de-Calais');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        familleProduct = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', familleProductPageUrlPattern);
    });
  });
});
