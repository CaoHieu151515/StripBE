import { PackageDriverStatus } from 'app/shared/model/enumerations/package-driver-status.model';

export interface IPackageDriver {
  id?: number;
  packageID?: string | null;
  price?: number | null;
  name?: string | null;
  description?: string | null;
  time?: number | null;
  bonus?: number | null;
  status?: keyof typeof PackageDriverStatus | null;
}

export const defaultValue: Readonly<IPackageDriver> = {};
