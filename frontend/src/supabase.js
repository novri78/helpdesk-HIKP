// Supabase client
import { createClient } from '@supabase/supabase-js'

const supabaseUrl = 'https://cfzhvnijayxycovjnyay.supabase.co'
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmemh2bmlqYXl4eWNvdmpueWF5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjUwMTk3MzMsImV4cCI6MjA0MDU5NTczM30.HLu-W-LIdcydCZEdHC7Kl9Uin21hKYKcQSJYbAnmdTA'
const supabase = createClient(supabaseUrl, supabaseKey)

// Avoid top-level await so this module can be imported without blocking.
// Export a helper to fetch session information when needed by the app.
export async function fetchSession() {
	const { data, error } = await supabase.auth.getSession();
	if (error) return { data: null, error };
	return { data, error: null };
}

export default supabase;