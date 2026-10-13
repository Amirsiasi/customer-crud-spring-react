import React, { useEffect, useState } from 'react'

const API = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export default function App() {
  const [customers, setCustomers] = useState([])
  const [form, setForm] = useState({ id: null, firstName: '', lastName: '', email: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const load = async () => {
    setLoading(true); setError(null)
    try {
      const res = await fetch(`${API}/api/customers`)
      const data = await res.json()
      setCustomers(data)
    } catch (e) {
      setError('Backend not reachable. Is it running on 8080?')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const onSubmit = async (e) => {
    e.preventDefault()
    const method = form.id ? 'PUT' : 'POST'
    const url = form.id ? `${API}/api/customers/${form.id}` : `${API}/api/customers`
    await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstName: form.firstName, lastName: form.lastName, email: form.email })
    })
    setForm({ id: null, firstName: '', lastName: '', email: '' })
    await load()
  }

  const edit = (c) => setForm(c)
  const remove = async (id) => {
    await fetch(`${API}/api/customers/${id}`, { method: 'DELETE' })
    await load()
  }

  return (
    <div style={{ maxWidth: 900, margin: '20px auto', fontFamily: 'system-ui, Arial' }}>
      <h1>Customer CRUD</h1>

      <form onSubmit={onSubmit} style={{ display: 'grid', gap: 8, gridTemplateColumns: 'repeat(4, 1fr)', alignItems: 'end', marginBottom: 20 }}>
        <div>
          <label>First Name</label>
          <input value={form.firstName} onChange={e => setForm({ ...form, firstName: e.target.value })} required
                 placeholder="Jane" style={{ width: '100%', padding: 8 }} />
        </div>
        <div>
          <label>Last Name</label>
          <input value={form.lastName} onChange={e => setForm({ ...form, lastName: e.target.value })} required
                 placeholder="Doe" style={{ width: '100%', padding: 8 }} />
        </div>
        <div>
          <label>Email</label>
          <input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required
                 placeholder="jane@doe.com" style={{ width: '100%', padding: 8 }} />
        </div>
        <button type="submit" style={{ padding: 10 }}>{form.id ? 'Update' : 'Create'}</button>
      </form>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <table width="100%" cellPadding="8" style={{ borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th align="left">ID</th>
            <th align="left">First</th>
            <th align="left">Last</th>
            <th align="left">Email</th>
            <th align="left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(c => (
            <tr key={c.id} style={{ borderTop: '1px solid #ddd' }}>
              <td>{c.id}</td>
              <td>{c.firstName}</td>
              <td>{c.lastName}</td>
              <td>{c.email}</td>
              <td>
                <button onClick={() => edit(c)} style={{ marginRight: 8 }}>Edit</button>
                <button onClick={() => remove(c.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
