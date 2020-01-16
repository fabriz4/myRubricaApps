export interface IContatti {
  id?: number;
  name?: string;
  surname?: string;
  phone?: string;
  email?: string;
  owner?: string;
}

export class Contatti implements IContatti {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public phone?: string,
    public email?: string,
    public owner?: string
  ) {}
}
