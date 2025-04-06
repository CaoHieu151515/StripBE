// export interface UserAccountDTO {
//   id: number;
//   name: string;
//   phone: string;
//   email: string;
//   gender: USER_ACCOUNT_GENDER;
//   status: USER_ACCOUNT_STATUS;
//   role: USER_ACCOUNT_ROLE;
// }
export interface UserDTO {
  userId: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  gender: string;
  phoneNumber: string;
  active: boolean;
  roles: string[];
}
