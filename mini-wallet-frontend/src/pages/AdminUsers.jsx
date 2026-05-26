import {
    useEffect,
    useState
} from "react";

import {
    useNavigate
} from "react-router-dom";

import api from "../api/axios";

function AdminUsers() {

    const navigate = useNavigate();

    const [users, setUsers] =
        useState([]);

    const [search, setSearch] =
        useState("");

    const fetchUsers = async () => {

        try {

            const response =
                await api.get(
                    "/api/admin/users"
                );

            setUsers(
                response.data
            );

        } catch (err) {

            console.log(err);
        }
    };

    useEffect(() => {

        fetchUsers();

    }, []);

    const filteredUsers =
        users.filter((user) =>

            user.mobileNumber
                ?.includes(search)
        );

    const logout = () => {

        const confirmed =

            window.confirm(

                "Are you sure you want to logout?"
            );

        if (!confirmed) {

            return;
        }

        localStorage.clear();

        navigate("/login");
    };

   const toggleStatus =
       async (id) => {

           try {

               await api.put(

                   `/api/admin/users/${id}/toggle-status`
               );

               fetchUsers();

           } catch (err) {

               console.log(err);
           }
       };

    return (

        <div className="flex min-h-screen bg-slate-950 text-white">

            {/* SIDEBAR */}

            <div className="w-72 bg-slate-900 border-r border-slate-800 p-6 flex flex-col">

                <h1 className="text-3xl font-bold mb-12">

                    Mini Wallet

                </h1>

                <div className="space-y-4">

                    <button
                        onClick={() =>
                            navigate("/admin-dashboard")
                        }
                        className="w-full bg-slate-800 hover:bg-slate-700 p-4 rounded-2xl text-left"
                    >

                        Dashboard

                    </button>

                    <button
                        className="w-full bg-emerald-500 text-white p-4 rounded-2xl text-left"
                    >

                        Users

                    </button>

                    <button
                        onClick={() =>
                            navigate("/admin/transactions")
                        }
                        className="w-full bg-slate-800 hover:bg-slate-700 p-4 rounded-2xl text-left"
                    >

                        Transactions

                    </button>

                </div>

                <div className="mt-auto">

                    <button
                        onClick={logout}
                        className="w-full bg-red-500 hover:bg-red-600 p-4 rounded-2xl"
                    >

                        Logout

                    </button>

                </div>

            </div>

            {/* MAIN */}

            <div className="flex-1 p-10">

                <div className="flex items-center justify-between mb-8">

                    <h1 className="text-4xl font-bold">

                        Users

                    </h1>

                    <input
                        type="text"
                        placeholder="Search by mobile"
                        value={search}
                        onChange={(e) =>
                            setSearch(
                                e.target.value
                            )
                        }
                        className="bg-slate-900 border border-slate-700 p-4 rounded-2xl w-80"
                    />

                </div>

                <div className="bg-slate-900 rounded-3xl overflow-hidden border border-slate-800">

                    <table className="w-full">

                        <thead className="bg-slate-800">

                        <tr>

                            <th className="text-left p-5">

                                User ID

                            </th>

                            <th className="text-left p-5">

                                Name

                            </th>

                            <th className="text-left p-5">

                                Mobile

                            </th>

                            <th className="text-left p-5">

                                Wallet Balance

                            </th>

                            <th className="text-left p-5">

                                Role

                            </th>

                            <th className="text-left p-5">

                                Status

                            </th>

                            <th className="text-left p-5">

                                Action

                            </th>

                        </tr>

                        </thead>

                        <tbody>

                        {filteredUsers.map((user) => (

                            <tr
                                key={user.id}
                                className="border-t border-slate-800 hover:bg-slate-800/40"
                            >

                                <td className="p-5">

                                    {user.id}

                                </td>

                                <td className="p-5">

                                    {user.name}

                                </td>

                                <td className="p-5">

                                    {user.mobileNumber}

                                </td>

                                <td className="p-5 text-emerald-400 font-semibold">

                                    ₹ {user.balance}

                                </td>

                                <td className="p-5">

                                    {user.role}

                                </td>

                                <td className="p-5">

                                    <span className={`px-3 py-1 rounded-full text-sm ${

                                        user.active

                                            ? "bg-emerald-500/20 text-emerald-400"

                                            : "bg-red-500/20 text-red-400"

                                    }`}>

                                        {
                                            user.active

                                                ? "ACTIVE"

                                                : "INACTIVE"
                                        }

                                    </span>

                                </td>

                                <td className="p-5">

                                    <button

                                        onClick={() =>
                                            toggleStatus(user.id)
                                        }

                                        className={`px-4 py-2 rounded-xl text-white ${

                                            user.active

                                                ? "bg-red-500 hover:bg-red-600"

                                                : "bg-emerald-500 hover:bg-emerald-600"
                                        }`}
                                    >

                                        {
                                            user.active

                                                ? "Deactivate"

                                                : "Activate"
                                        }

                                    </button>

                                </td>

                            </tr>

                        ))}

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
    );
}

export default AdminUsers;