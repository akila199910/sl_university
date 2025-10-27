export type User = 
    { 
        id: number; 
        email: string;
        name : string, 
        role: {
            id:number,
            name:string
        },
        contactNumber: string,
        status : boolean,
        profile:{
            id:number,
            profileUrl:string;
            coverUrl:string
        },
        editUrl:string | null;
        viewUrl:string | null;
        deleteUrl:string | null
    };