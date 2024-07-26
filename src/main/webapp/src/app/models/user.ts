import { JobTitle } from './type';

export class UserData {
  userId?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  location?: string;
  phone?: string;
  jobTitle?: JobTitle;
  image?: string;
  creationDate?: Date;
  lastUpdateDate?: Date;
}
