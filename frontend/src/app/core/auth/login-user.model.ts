export class LoginUser {
  username: string;
  password: string;

  constructor(values: Object = {}) {
    Object.assign(this, values);
  }
}
