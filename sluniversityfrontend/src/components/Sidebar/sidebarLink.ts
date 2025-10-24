const sidebarLinks = [
    { id: 1, name: 'Dashboard', link: '/dashboard' , icon: 'DA'},
    { id: 2, name: 'Profile', link: '/profile' , icon: 'PR'},
    { id: 3, name: 'Settings', link: '/settings', icon: 'SE' },
    { id: 4, name: 'Help', link: '/help', icon: 'HE' },
    { id: 5, name: 'About', link: '/about', icon: 'AB' },
    { id: 6, name: 'Logout', link: '/logout', icon: 'LO' },
    { id: 15, name: 'Users', link: '/users', icon: 'US', multy: true, 
        subLinks: [
            { id: 1, name: 'Admins', link: '/admins', icon: 'AU' },
            { id: 2, name: 'Users', link: '/users', icon: 'Us' },
        ]
     },
    { id: 7, name: 'Dashboard', link: '/dashboard' , icon: 'DA', multy: true,
        subLinks: [
            { id: 1, name: 'Admins', link: '/admins', icon: 'AU' },
            { id: 2, name: 'Users', link: '/users', icon: 'Us' },
        ]},
    { id: 8, name: 'Profile', link: '/profile' , icon: 'PR'},
    { id: 9, name: 'Settings', link: '/settings', icon: 'SE' },
    { id: 10, name: 'Help', link: '/help', icon: 'HE' },
    { id: 11, name: 'About', link: '/about', icon: 'AB' },
    { id: 12, name: 'Logout', link: '/logout', icon: 'LO' },
    { id: 13, name: 'Logout', link: '/logout', icon: 'LO' },
    { id: 14, name: 'Logout', link: '/logout', icon: 'LO' },
];

export default sidebarLinks;