import { useEffect, useState } from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import PageLayout from '@components/layouts/PaageLayout';

const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 120 },
    { field: 'host', headerName: 'ID', width: 120 },
    { field: 'summary', headerName: 'ID', width: 120 },
    { field: 'description', headerName: 'ID', width: 120 },
];

interface HostRow {
    id: number;
    host: string;
    summary: string;
    description: string;
}

export default function Host() {

    const [rows, setRows] = useState<HostRow[]>([]);

    useEffect(() => {
        setRows([
            {
                id: 1,
                host: 'google',
                summary: 'google',
                description: 'google',
            },
        ]);
    }, []);

    return (
        <PageLayout>
            <DataGrid columns={columns} rows={rows} />
        </PageLayout>
    );
}
