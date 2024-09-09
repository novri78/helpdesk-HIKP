<template>
  <div class="ask-page">
    <h1>Ask Wi*ipe*ia Page</h1>
    <div class="main-content">
      <!-- Side AdSense Placeholder -->
      <div class="side-ads">
        <!-- Placeholder iklan (ganti dengan script AdSense Anda) -->
        <!-- <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
        <script>
          (adsbygoogle = window.adsbygoogle || []).push({
            google_ad_client: "pub-4155300425087945",
            enable_page_level_ads: true,
          });
        </script> -->
      </div>

      <!-- Main Content -->
      <div class="content">
        <form @submit.prevent="searchWikipedia">
          <input v-model="query" type="text" placeholder="Ketik pertanyaan Anda di sini..." required />
          <select v-model="language">
            <option value="id">Indonesia</option>
            <option value="en">English</option>
          </select>
          <button type="submit">Cari Jawaban</button>
        </form>

        <!-- Result Section -->
        <div class="result">
          <p v-if="loading">Mencari...</p>
          <p v-else-if="error">{{ error }}</p>
          <div v-else-if="result">
            <h2>{{ result.title }}</h2>
            <img v-if="result.thumbnail" :src="result.thumbnail" alt="Gambar" />
            <!-- <img v-if="result.thumbnail" :src="result.thumbnail" alt="Thumbnail" /> -->
            <p v-else>Gambar tidak tersedia untuk artikel ini.</p>

            <!-- Render the extract inside a div with the class "infobox vcard" -->
            <div class="infobox vcard" v-html="result.extract"></div>
            <p>
              <a :href="result.link" target="_blank">Baca lebih lanjut di Wikipedia</a>
            </p>
            <h3>Informasi di Infobox</h3>
            <ul v-if="result.infobox">
              <li v-for="(item, index) in result.infobox" :key="index">
                <strong>{{ item.label }}:</strong> {{ item.value }}
              </li>
            </ul>
          </div>
          <p v-else>Masukkan pertanyaan di atas untuk mencari jawaban.</p>
        </div>
      </div>

      <!-- Side AdSense Placeholder -->
      <div class="side-ads">
        <!-- Placeholder iklan (ganti dengan script AdSense Anda) -->
        <!-- <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
        <script>
          (adsbygoogle = window.adsbygoogle || []).push({
            google_ad_client: "pub-4155300425087945",
            enable_page_level_ads: true,
          });
        </script> -->
      </div>
    </div>

    <!-- AdSense Placeholder -->
    <div class="adsense-placeholder">
      <!-- Placeholder iklan (ganti dengan script AdSense Anda) -->
      <!-- <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
      <script>
        (adsbygoogle = window.adsbygoogle || []).push({
          google_ad_client: "pub-4155300425087945",
          slot: "YOUR_SLOT_ID_728x90",
          format: "auto",
        });
      </script> -->
    </div>

    <div class="adsense-placeholder">
      <!-- Placeholder iklan (ganti dengan script AdSense Anda) -->
      <!-- <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
      <script>
        (adsbygoogle = window.adsbygoogle || []).push({
          google_ad_client: "pub-4155300425087945",
          slot: "YOUR_SLOT_ID_300x250",
          format: "auto",
        });
      </script> -->
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const query = ref('');
const language = ref('id');
const result = ref(null);
const loading = ref(false);
const error = ref(null);

const searchWikipedia = async () => {
  if (!query.value) return;

  loading.value = true;
  error.value = null;
  result.value = null;

  try {
    const response = await fetch(
      `https://${language.value}.wikipedia.org/w/api.php?action=query&format=json&origin=*&prop=pageimages|extracts|revisions&exintro&explaintext&generator=search&gsrsearch=${encodeURIComponent(
        query.value
      )}&gsrlimit=1&piprop=thumbnail&pithumbsize=500&rvprop=content`
    );
    const data = await response.json();
    const pages = data.query?.pages;
    const pageId = pages ? Object.keys(pages)[0] : null;

    if (!pageId || pageId === '-1') {
      error.value = 'Kami tidak menemukan data tersebut.';
    } else {
      const page = pages[pageId];
      result.value = {
        title: page.title,
        extract: page.extract ? `<div class="infobox vcard">${page.extract}</div>` : '',
        thumbnail: page.thumbnail ? page.thumbnail.source : null,
        link: `https://${language.value}.wikipedia.org/?curid=${pageId}`,
      };

      // Additional request to fetch full page content for infobox
      const pageContentResponse = await fetch(
        `https://${language.value}.wikipedia.org/w/api.php?action=parse&pageid=${pageId}&format=json&prop=text&origin=*`
      );
      const pageContentData = await pageContentResponse.json();
      const pageContent = pageContentData.parse?.text['*'];

      // Parse to extract infobox labels and values
      const parser = new DOMParser();
      const doc = parser.parseFromString(pageContent, 'text/html');
      const infobox = doc.querySelector('.infobox');

      if (infobox) {
        const labels = Array.from(infobox.querySelectorAll('.infobox-label')).map(
          (label) => label.textContent
        );
        const values = Array.from(infobox.querySelectorAll('.infobox-data')).map(
          (value) => value.textContent
        );
        result.value.infobox = labels.map((label, i) => ({
          label,
          value: values[i],
        }));
      }
    }
  } catch (err) {
    error.value = 'Terjadi kesalahan. Coba lagi nanti.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* Global Styles */
.ask-page {
  font-family: "Poppins", sans-serif;
  margin: 70px;
  padding: 0;
  background-color: #f4f4f4;
  color: #333;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh;
  perspective: 1000px;
}

.main-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
  max-width: 900px;
}

.side-ads {
  width: 90px;
  height: 600px;
  background: #e9e9e9;
  display: inline-block;
  margin: 0 10px;
  border-radius: 8px;
  border: 1px dashed #ccc;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.content form {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.content input[type="text"] {
  padding: 12px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
}

.content select {
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
}

.content button {
  padding: 12px;
  background: linear-gradient(135deg, #679a83, #19b16f);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s ease;
}

.content button:hover {
  background: linear-gradient(135deg, #0056b3, #003f87);
}

.result {
  margin-top: 20px;
  padding: 15px;
  background: #f0f0f0;
  border-radius: 8px;
}

.result p {
  margin: 0;
  font-size: 1rem;
  line-height: 1.5;
}

.result h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.result img {
  max-width: 100%;
  height: auto;
  margin-bottom: 15px;
  border-radius: 8px;
}

.result a {
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
}

.result a:hover {
  text-decoration: underline;
}

.adsense-placeholder {
  width: 100%;
  height: 90px;
  background: #e9e9e9;
  margin: 20px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  color: #666;
  border-radius: 8px;
  border: 1px dashed #ccc;
}

/* Responsive Design */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
    align-items: center;
  }

  .side-ads {
    width: 100%;
    height: 90px;
    margin: 10px 0;
  }
}
</style>
