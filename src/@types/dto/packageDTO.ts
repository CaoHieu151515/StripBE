export interface PackageGetAllDTO {
  index?: number;
  id?: number;
  packageID?: string;
  price?: number;
  name?: string;
  description?: string;
  time?: number;
  bonus?: number;
  status?: string;
  payments?: Payment[];
  driverPackageSubscriptions?: DriverPackageSubscription[];
}

export interface DriverPackageSubscription {
  id?: string;
  purchaseDate?: Date;
  expirationDate?: Date;
  packagePrice?: number;
  active?: boolean;
}

export interface Payment {
  id?: number;
  paymentID?: string;
  amount?: number;
  paymentDate?: Date;
  paymentStatus?: string;
  transactionId?: string;
}
