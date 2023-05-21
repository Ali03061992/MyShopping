export interface IFamilleProduct {
  id: number;
  name?: string | null;
  type?: string | null;
}

export type NewFamilleProduct = Omit<IFamilleProduct, 'id'> & { id: null };
