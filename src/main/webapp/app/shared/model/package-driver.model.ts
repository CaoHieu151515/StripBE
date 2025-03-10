export interface IPackageDriver {
  id?: number;
  packageID?: string | null;
  price?: number | null;
  name?: string | null;
  description?: string | null;
  time?: number | null;
  bonus?: number | null;
}

export const defaultValue: Readonly<IPackageDriver> = {};
